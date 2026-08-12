import http from "k6/http";
import { check } from "k6";

export const options = {
    stages: [
        { duration: "30s", target: 25 },
        { duration: "30s", target: 50 },
        { duration: "30s", target: 100 },
        { duration: "30s", target: 200 },
        { duration: "30s", target: 300 },
        { duration: "30s", target: 500 },
        { duration: "1m", target: 500 },
        { duration: "30s", target: 0 }
    ]
};

const BASE_URL = "http://localhost:8080";

export default function () {

    const id = `${__VU}-${__ITER}`;
    const username = `stressuser${id}`;

    const headers = {
        headers: {
            "Content-Type": "application/json"
        }
    };

    // SIGNUP
    const signupRes = http.post(
        `${BASE_URL}/auth/signup`,
        JSON.stringify({
            username: username,
            password: "password123",
            email: `${username}@test.com`
        }),
        headers
    );

    if (!check(signupRes, {
        "signup successful": r => r.status === 200 || r.status === 201
    })) {
        console.log(
            `SIGNUP FAILED ${username}: ${signupRes.status}`
        );
        return;
    }

    // LOGIN
    const loginRes = http.post(
        `${BASE_URL}/auth/login`,
        JSON.stringify({
            username: username,
            password: "password123"
        }),
        headers
    );

    if (!check(loginRes, {
        "login successful": r => r.status === 200
    })) {
        console.log(
            `LOGIN FAILED ${username}: ${loginRes.status}`
        );
        return;
    }

    const loginData = JSON.parse(loginRes.body);

    const token = loginData.token;
    const userId = loginData.userId;

    const authHeaders = {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    };

    // GET ACCOUNTS
    const accountRes = http.get(
        `${BASE_URL}/users/${userId}/accounts`,
        authHeaders
    );

    if (!check(accountRes, {
        "get accounts successful": r => r.status === 200
    })) {
        console.log(
            `ACCOUNT GET FAILED ${username}: ${accountRes.status}`
        );
        return;
    }

    const accounts = JSON.parse(accountRes.body);

    if (accounts.length === 0) {
        return;
    }

    const accountId = accounts[0].accountId;

    // DEPOSIT
    const depositRes = http.post(
        `${BASE_URL}/users/${userId}/accounts/${accountId}/deposit`,
        JSON.stringify({
            amount: 500
        }),
        authHeaders
    );

    check(depositRes, {
        "deposit successful": r => r.status === 200
    });

    // WITHDRAW
    const withdrawRes = http.post(
        `${BASE_URL}/users/${userId}/accounts/${accountId}/withdraw`,
        JSON.stringify({
            amount: 100
        }),
        authHeaders
    );

    check(withdrawRes, {
        "withdraw successful": r => r.status === 200
    });

    // CREATE ACCOUNT
    const createRes = http.post(
        `${BASE_URL}/users/${userId}/accounts/create`,
        JSON.stringify({
            accountType: "CHECKING"
        }),
        authHeaders
    );

    if (!check(createRes, {
        "account creation successful": r => r.status === 200
    })) {
        return;
    }

    const newAccount = JSON.parse(createRes.body);
    const newAccountId = newAccount.accountId;

    // DELETE ACCOUNT
    const removeRes = http.del(
        `${BASE_URL}/users/${userId}/accounts/${newAccountId}`,
        null,
        authHeaders
    );

    check(removeRes, {
        "account removal successful": r => r.status === 200
    });
}