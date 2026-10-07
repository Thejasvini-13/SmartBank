/* =========================================================
   SmartBank Accounts Management Page Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadAccounts();

    document.getElementById("open-account-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const customerId = document.getElementById("acc-customer-id").value;
        const accountType = document.getElementById("acc-type").value;
        const initialDeposit = parseFloat(document.getElementById("acc-deposit").value);

        try {
            const newAcc = await API.request(`/accounts?customerId=${customerId}&accountType=${accountType}&initialDeposit=${initialDeposit}`, { method: "POST" });
            showToast(`Account ${newAcc.accountNumber} created successfully!`);
            closeModal('account-modal');
            loadAccounts();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });

    document.getElementById("deposit-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const accNo = document.getElementById("dep-acc-no").value;
        const amount = parseFloat(document.getElementById("dep-amount").value);
        const desc = document.getElementById("dep-desc").value;

        try {
            await API.deposit(accNo, amount, desc);
            showToast(`Deposited ₹${amount} into ${accNo} successfully!`);
            closeModal('deposit-modal');
            loadAccounts();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });

    document.getElementById("withdraw-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const accNo = document.getElementById("with-acc-no").value;
        const amount = parseFloat(document.getElementById("with-amount").value);
        const desc = document.getElementById("with-desc").value;

        try {
            await API.withdraw(accNo, amount, desc);
            showToast(`Withdrew ₹${amount} from ${accNo} successfully!`);
            closeModal('withdraw-modal');
            loadAccounts();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });

    document.getElementById("transfer-form")?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const fromAcc = document.getElementById("trans-from-acc").value;
        const toAcc = document.getElementById("trans-to-acc").value;
        const amount = parseFloat(document.getElementById("trans-amount").value);
        const desc = document.getElementById("trans-desc").value;

        try {
            await API.transfer(fromAcc, toAcc, amount, desc);
            showToast(`Transferred ₹${amount} from ${fromAcc} to ${toAcc}!`);
            closeModal('transfer-modal');
            loadAccounts();
        } catch (err) {
            showToast(err.message, "danger");
        }
    });
});

async function loadAccounts() {
    const list = document.getElementById("accounts-list");
    if (!list) return;

    try {
        const accounts = await API.getAccounts();
        list.innerHTML = "";

        accounts.forEach(acc => {
            const card = document.createElement("div");
            card.className = "card";
            card.innerHTML = `
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;">
                    <span class="badge ${acc.accountType === 'SAVINGS' ? 'badge-info' : 'badge-warning'}">${acc.accountType} ACCOUNT</span>
                    <span class="badge badge-success">${acc.status}</span>
                </div>
                <div style="font-size:1.1rem; font-weight:700; color:#0f172a; margin-bottom:4px;">${acc.accountNumber}</div>
                <div style="font-size:0.85rem; color:#64748b; margin-bottom:16px;">Holder: ${acc.customer.name}</div>
                <div class="card-subtitle">Current Balance</div>
                <div class="card-value" style="color:#2563eb;">₹${acc.balance.toLocaleString('en-IN')}</div>
                <div style="margin-top:16px; font-size:0.8rem; color:#64748b;">
                    ${acc.accountType === 'SAVINGS' ? `Interest Rate: ${acc.interestRate}% p.a.` : `Overdraft Limit: ₹${acc.overdraftLimit.toLocaleString('en-IN')}`}
                </div>
            `;
            list.appendChild(card);
        });
    } catch (err) {
        showToast("Error fetching accounts: " + err.message, "danger");
    }
}

function openModal(id) {
    document.getElementById(id).classList.add("active");
}

function closeModal(id) {
    document.getElementById(id).classList.remove("active");
}
