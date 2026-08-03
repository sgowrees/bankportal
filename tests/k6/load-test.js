import http from "k6/http";
import { check } from "k6";

export const options = {
    vus: 50,
    iterations: 50
};

const BASE_URL = "http://localhost:8080";

export default function () {

    const id = __VU;
    const username = `loaduser${id}-0`;

    const headers = {
        headers:{
            "Content-Type":"application/json"
        }
    };


    const signupRes = http.post(
        `${BASE_URL}/auth/signup`,
        JSON.stringify({
            username: username,
            password:"password123",
            email:`${username}@test.com`
        }),
        headers
    );


    if(!check(signupRes,{
        "user created":
            r=>r.status === 200 || r.status === 201
    })){
        console.log(
            `SIGNUP FAILED ${username}: ${signupRes.status} ${signupRes.body}`
        );
        return;
    }



    const loginRes = http.post(
        `${BASE_URL}/auth/login`,
        JSON.stringify({
            username:username,
            password:"password123"
        }),
        headers
    );


    if(!check(loginRes,{
        "login successful":
            r=>r.status === 200
    })){
        console.log(
            `LOGIN FAILED ${username}: ${loginRes.status} ${loginRes.body}`
        );
        return;
    }



    const token = JSON.parse(loginRes.body).token;


    const authHeaders = {
        headers:{
            "Authorization":`Bearer ${token}`,
            "Content-Type":"application/json"
        }
    };



    const accountRes = http.get(
        `${BASE_URL}/accounts`,
        authHeaders
    );


    if(!check(accountRes,{
        "get accounts":
            r=>r.status === 200
    })){
        console.log(
            `ACCOUNT GET FAILED ${username}: ${accountRes.status} ${accountRes.body}`
        );
        return;
    }



    const accounts = JSON.parse(accountRes.body);

    const defaultAccountId = accounts[0].accountId;



    // DEPOSIT

    const depositRes = http.post(
        `${BASE_URL}/accounts/${defaultAccountId}/deposit`,
        JSON.stringify({
            amount:500
        }),
        authHeaders
    );


    check(depositRes,{
        "deposit successful":
            r=>r.status === 200
    });



    // WITHDRAW

    const withdrawRes = http.post(
        `${BASE_URL}/accounts/${defaultAccountId}/withdraw`,
        JSON.stringify({
            amount:100
        }),
        authHeaders
    );


    check(withdrawRes,{
        "withdraw successful":
            r=>r.status === 200
    });



    // CREATE SECOND ACCOUNT

    const createRes = http.post(
        `${BASE_URL}/accounts/create`,
        JSON.stringify({
            accountType:"CHECKING"
        }),
        authHeaders
    );


    if(createRes.status !== 200){
        console.log(
            `CREATE ACCOUNT FAILED ${username}: ${createRes.status} ${createRes.body}`
        );
        return;
    }



    const newAccount = JSON.parse(createRes.body);

    const deleteAccountId = newAccount.accountId;



    // REMOVE SECOND ACCOUNT

    const removeRes = http.del(
        `${BASE_URL}/accounts/${deleteAccountId}`,
        null,
        authHeaders
    );


    check(removeRes,{
        "account removed":
            r=>r.status === 200
    });


}