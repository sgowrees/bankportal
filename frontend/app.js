const API_BASE_URL = 'http://localhost:8080';
const state = {
  user: null,
  accounts: [],
  creditAccounts: [],
  message: '',
  error: '',
};

const app = document.getElementById('app');

async function ensureCurrentUser() {
  const token = localStorage.getItem('bankportal-token');
  if (!token) {
    state.user = null;
    return null;
  }

  if (state.user?.id) {
    return state.user;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/auth/me`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      throw new Error('Unable to resolve current user');
    }

    const user = await response.json();
    state.user = { id: user.id, username: user.username };
    return state.user;
  } catch (error) {
    localStorage.removeItem('bankportal-token');
    state.user = null;
    throw error;
  }
}

function render() {
  if (!state.user) {
    app.innerHTML = `
      <main class="page-shell">
        <section class="card auth-card">
          <div class="brand-row">
            <div class="brand-mark">BP</div>
            <div>
              <h1>BankPortal</h1>
              <p>Modern banking for everyday life</p>
            </div>
          </div>

          <form id="signup-toggle-form" class="inline-toggle">
            <button type="button" id="show-login" class="ghost-btn small">Login</button>
            <button type="button" id="show-signup" class="ghost-btn small active">Sign up</button>
          </form>

          <form id="auth-form" class="form-stack">
            <label for="username">Username</label>
            <input id="username" name="username" autocomplete="username" required />

            <label for="password">Password</label>
            <input id="password" name="password" type="password" autocomplete="current-password" required />

            <button type="submit" id="auth-submit">Create account</button>
          </form>

          <p class="status" data-testid="login-error" aria-live="polite"></p>
          <p class="success" data-testid="login-success" aria-live="polite"></p>
        </section>
      </main>
    `;

    const authForm = document.getElementById('auth-form');
    const errorBox = document.querySelector('[data-testid="login-error"]');
    const successBox = document.querySelector('[data-testid="login-success"]');
    const submitButton = document.getElementById('auth-submit');
    const showLogin = document.getElementById('show-login');
    const showSignup = document.getElementById('show-signup');

    let mode = 'signup';

    const setMode = (nextMode) => {
      mode = nextMode;
      submitButton.textContent = nextMode === 'login' ? 'Login' : 'Create account';
      showLogin.classList.toggle('active', nextMode === 'login');
      showSignup.classList.toggle('active', nextMode === 'signup');
    };

    showLogin.addEventListener('click', () => setMode('login'));
    showSignup.addEventListener('click', () => setMode('signup'));

    authForm.addEventListener('submit', async (event) => {
      event.preventDefault();
      errorBox.textContent = '';
      successBox.textContent = '';

      const payload = {
        username: authForm.username.value.trim(),
        password: authForm.password.value,
      };

      try {
        const endpoint = mode === 'login' ? '/auth/login' : '/auth/signup';
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        });

        const data = await response.text();

        if (!response.ok) {
          throw new Error(data || 'Request failed');
        }

        if (mode === 'signup') {
          successBox.textContent = 'Account created. You can now sign in.';
          setMode('login');
          authForm.reset();
          return;
        }

        const loginData = JSON.parse(data);
        localStorage.setItem('bankportal-token', loginData.token);
        const currentUser = await ensureCurrentUser();
        state.user = currentUser || { id: loginData.userId || loginData.id, username: payload.username };
        successBox.textContent = `Welcome back, ${state.user.username}!`;
        render();
      } catch (error) {
        errorBox.textContent = error.message || 'Unable to complete request.';
      }
    });

    setMode('signup');
    return;
  }

  app.innerHTML = `
    <main class="dashboard-shell">
      <aside class="sidebar">
        <div>
          <h2>BankPortal</h2>
          <p>Welcome, ${state.user.username}</p>
        </div>
        <button id="logout-btn">Logout</button>
      </aside>

      <section class="main-panel">
        <header class="hero-card">
          <div>
            <p class="eyebrow">Overview</p>
            <h3>Banking controls</h3>
          </div>
          <div class="balance-pill">${state.message || 'Ready'}</div>
        </header>

        <section class="card-grid">
          <article class="card panel-card">
            <div class="section-title-row">
              <h4>Accounts</h4>
              <div class="button-row">
                <button class="ghost-btn small" id="create-account-btn">Create account</button>
                <button class="ghost-btn small" id="refresh-btn">Refresh</button>
              </div>
            </div>
            <div id="accounts-list" class="list-stack"></div>
          </article>

          <article class="card panel-card">
            <div class="section-title-row">
              <h4>Credit Cards</h4>
              <button class="ghost-btn small" id="create-card-btn">Create card</button>
            </div>
            <div id="credit-list" class="list-stack"></div>
          </article>
        </section>

        <section class="card panel-card action-card">
          <div class="section-title-row">
            <h4>Quick actions</h4>
          </div>
          <div class="action-grid">
            <div class="action-box" id="account-box">
              <h5>Account</h5>
              <select id="account-select"></select>
            </div>
            <div class="action-box" id="deposit-box">
              <h5>Deposit</h5>
              <input id="deposit-amount" type="number" step="0.01" placeholder="Amount" />
              <button id="deposit-btn">Deposit</button>
            </div>
            <div class="action-box" id="withdraw-box">
              <h5>Withdraw</h5>
              <input id="withdraw-amount" type="number" step="0.01" placeholder="Amount" />
              <button id="withdraw-btn">Withdraw</button>
            </div>
            <div class="action-box" id="transfer-box">
              <h5>Transfer</h5>
              <input id="transfer-amount" type="number" step="0.01" placeholder="Amount" />
              <input id="transfer-target" type="number" placeholder="To account id" />
              <button id="transfer-btn">Transfer</button>
            </div>
            <div class="action-box" id="charge-box">
              <h5>Charge card</h5>
              <input id="charge-amount" type="number" step="0.01" placeholder="Amount" />
              <button id="charge-btn">Charge</button>
            </div>
          </div>
        </section>
      </section>
    </main>
  `;

  document.getElementById('logout-btn').addEventListener('click', () => {
    localStorage.removeItem('bankportal-token');
    state.user = null;
    state.accounts = [];
    state.creditAccounts = [];
    state.message = '';
    state.error = '';
    render();
  });

  document.getElementById('refresh-btn').addEventListener('click', () => {
    loadData();
  });

  document.getElementById('create-account-btn').addEventListener('click', createAccount);
  document.getElementById('create-card-btn').addEventListener('click', createCreditCard);
  document.getElementById('deposit-btn').addEventListener('click', deposit);
  document.getElementById('withdraw-btn').addEventListener('click', withdraw);
  document.getElementById('transfer-btn').addEventListener('click', transfer);
  document.getElementById('charge-btn').addEventListener('click', chargeCard);

  const accountSelect = document.getElementById('account-select');
  if (accountSelect) {
    accountSelect.addEventListener('change', () => updateActionVisibility());
  }

  loadData();
}

async function loadData() {
  const token = localStorage.getItem('bankportal-token');
  const currentUser = await ensureCurrentUser();

  if (!token || !currentUser) {
    return;
  }

  try {
    const accountsResponse = await fetch(`${API_BASE_URL}/users/${currentUser.id}/accounts`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (accountsResponse.ok) {
      state.accounts = await accountsResponse.json();
    }

    const creditResponse = await fetch(`${API_BASE_URL}/users/${currentUser.id}/credit-accounts`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (creditResponse.ok) {
      state.creditAccounts = await creditResponse.json();
    }
  } catch (error) {
    console.error(error);
  }

  renderLists();
}

function getAccountType(account) {
  const rawType = (account.accountType || account.type || '').toString().toUpperCase();
  if (rawType === 'CREDIT' || rawType === 'CREDIT_ACCOUNT' || account.creditLimit != null || account.amountDue != null || account.amountPaid != null) {
    return 'CREDIT';
  }
  return 'DEBIT';
}

function getAccountBalance(account) {
  return Number(account.balance ?? account.amountDue ?? account.amountPaid ?? 0);
}

function renderLists() {
  const accountsList = document.getElementById('accounts-list');
  const creditList = document.getElementById('credit-list');
  const accountSelect = document.getElementById('account-select');

  const debitAccounts = state.accounts.filter((account) => getAccountType(account) !== 'CREDIT');
  const creditCards = state.creditAccounts.filter((account) => getAccountType(account) === 'CREDIT');

  accountsList.innerHTML = debitAccounts.length
    ? debitAccounts.map((account) => `
        <div class="list-item">
          <div>
            <strong>${account.accountNumber || 'Checking'}</strong>
            <p>${account.accountType || 'CHECKING'}</p>
          </div>
          <span>$${getAccountBalance(account).toFixed(2)}</span>
        </div>
      `).join('')
    : '<p class="muted">No accounts yet.</p>';

  if (accountSelect) {
    const selectedValue = accountSelect.value || '';
    const options = [];

    if (debitAccounts.length) {
      debitAccounts.forEach((account) => {
        const label = `${account.accountNumber || account.accountId} — $${getAccountBalance(account).toFixed(2)}`;
        options.push(`<option value="${account.accountId}" data-type="${getAccountType(account)}">${label}</option>`);
      });
    }

    if (creditCards.length) {
      creditCards.forEach((account) => {
        const label = `${account.accountNumber || account.accountId} (Card) — $${getAccountBalance(account).toFixed(2)}`;
        options.push(`<option value="${account.accountId}" data-type="CREDIT">${label}</option>`);
      });
    }

    accountSelect.innerHTML = options.length ? options.join('') : '<option value="">No accounts</option>';

    if (selectedValue) {
      const matchingOption = Array.from(accountSelect.options).find((option) => option.value === selectedValue);
      if (matchingOption) {
        accountSelect.value = selectedValue;
      }
    }
  }

  creditList.innerHTML = creditCards.length
    ? creditCards.map((account) => `
        <div class="list-item">
          <div>
            <strong>${account.accountNumber || 'Card'}</strong>
            <p>Limit $${Number(account.creditLimit || 0).toFixed(2)}</p>
          </div>
          <span>$${getAccountBalance(account).toFixed(2)}</span>
        </div>
      `).join('')
    : '<p class="muted">No credit cards yet.</p>';

  updateActionVisibility();
}

function updateActionVisibility() {
  const accountSelect = document.getElementById('account-select');
  const depositBox = document.getElementById('deposit-box');
  const withdrawBox = document.getElementById('withdraw-box');
  const transferBox = document.getElementById('transfer-box');
  const chargeBox = document.getElementById('charge-box');

  if (!accountSelect) return;

  const selectedOption = accountSelect.options[accountSelect.selectedIndex];
  const type = selectedOption ? (selectedOption.dataset.type || 'DEBIT') : 'DEBIT';

  const isCredit = type === 'CREDIT' || type === 'CREDIT_ACCOUNT' || type === 'CREDIT';

  if (isCredit) {
    // show credit UI only
    if (depositBox) depositBox.style.display = 'none';
    if (withdrawBox) withdrawBox.style.display = 'none';
    if (transferBox) transferBox.style.display = 'none';
    if (chargeBox) chargeBox.style.display = 'block';
  } else {
    // show non-credit UI
    if (depositBox) depositBox.style.display = 'block';
    if (withdrawBox) withdrawBox.style.display = 'block';
    if (transferBox) transferBox.style.display = 'block';
    if (chargeBox) chargeBox.style.display = 'none';
  }
}

async function refreshDashboard() {
  await loadData();
  const balancePill = document.querySelector('.balance-pill');
  if (balancePill) {
    balancePill.textContent = state.message || 'Ready';
  }
}

async function createAccount() {
  const token = localStorage.getItem('bankportal-token');
  const currentUser = await ensureCurrentUser();

  if (!token || !currentUser) {
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}/accounts/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ accountType: 'CHECKING' }),
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || 'Unable to create account');
    }

    state.message = `Created ${data.accountNumber || 'account'}`;
    await refreshDashboard();
  } catch (error) {
    state.error = error.message;
    render();
  }
}

async function createCreditCard() {
  const token = localStorage.getItem('bankportal-token');
  const currentUser = await ensureCurrentUser();

  if (!token || !currentUser) {
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}/credit-accounts/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        creditLimit: 5000,
        interestRate: 0.2,
        minPayment: 50,
        dailyLimit: 1000,
        accountType: 'CREDIT',
      }),
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || 'Unable to create credit card');
    }

    state.message = `Created ${data.accountNumber || 'credit card'}`;
    await refreshDashboard();
  } catch (error) {
    state.error = error.message;
    render();
  }
}

async function deposit() {
  const token = localStorage.getItem('bankportal-token');
  const currentUser = await ensureCurrentUser();

  if (!token || !currentUser || !state.accounts.length) {
    return;
  }

  const amount = document.getElementById('deposit-amount').value;
  const sel = document.getElementById('account-select');
  const accountId = (sel && sel.value) ? sel.value : (state.accounts[0] && state.accounts[0].accountId);

  try {
    const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}/accounts/${accountId}/deposit`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ accountId, amount }),
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || 'Deposit failed');
    }

    state.message = `Deposited $${amount}`;
    await refreshDashboard();
  } catch (error) {
    state.error = error.message;
    render();
  }
}

async function withdraw() {
  const token = localStorage.getItem('bankportal-token');
  const currentUser = await ensureCurrentUser();

  if (!token || !currentUser || !state.accounts.length) {
    return;
  }

  const amount = document.getElementById('withdraw-amount').value;
  const selW = document.getElementById('account-select');
  const accountId = (selW && selW.value) ? selW.value : (state.accounts[0] && state.accounts[0].accountId);

  try {
    const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}/accounts/${accountId}/withdraw`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ accountId, amount }),
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || 'Withdrawal failed');
    }

    state.message = `Withdrew $${amount}`;
    await refreshDashboard();
  } catch (error) {
    state.error = error.message;
    render();
  }
}

async function transfer() {
  const token = localStorage.getItem('bankportal-token');
  const currentUser = await ensureCurrentUser();

  if (!token || !currentUser || !state.accounts.length) {
    return;
  }

  const amount = document.getElementById('transfer-amount').value;
  const targetId = document.getElementById('transfer-target').value;
  const selT = document.getElementById('account-select');
  const accountId = (selT && selT.value) ? selT.value : (state.accounts[0] && state.accounts[0].accountId);

  try {
    const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}/accounts/${accountId}/transfer`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ accountId, toAccountId: targetId, amount }),
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || 'Transfer failed');
    }

    state.message = `Transferred $${amount}`;
    await refreshDashboard();
  } catch (error) {
    state.error = error.message;
    render();
  }
}

async function chargeCard() {
  const token = localStorage.getItem('bankportal-token');
  const currentUser = await ensureCurrentUser();

  if (!token || !currentUser || !state.creditAccounts.length) {
    return;
  }

  const amount = document.getElementById('charge-amount').value;
  const selC = document.getElementById('account-select');
  // charge should target a credit account; prefer selected account if it is a credit account id
  let accountId = (selC && selC.value) ? selC.value : (state.creditAccounts[0] && state.creditAccounts[0].accountId);
  // if accountSelect points to a checking/savings id but we only have creditAccounts, fall back
  if (!accountId && state.creditAccounts[0]) accountId = state.creditAccounts[0].accountId;

  try {
    const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}/credit-accounts/${accountId}/charge`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ accountId, amount }),
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || 'Charge failed');
    }

    state.message = `Charged $${amount}`;
    await refreshDashboard();
  } catch (error) {
    state.error = error.message;
    render();
  }
}

async function initializeApp() {
  const token = localStorage.getItem('bankportal-token');

  if (token) {
    try {
      await ensureCurrentUser();
    } catch (error) {
      console.warn(error);
    }
  }

  render();

  if (state.user) {
    loadData();
  }
}

initializeApp();
