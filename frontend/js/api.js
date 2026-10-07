/* =========================================================
   SmartBank Central Fetch API Client Wrapper
   ========================================================= */

const API_BASE_URL = "http://localhost:8080/api";

const API = {
    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const config = {
            headers: {
                "Content-Type": "application/json",
                ...options.headers
            },
            ...options
        };

        try {
            const response = await fetch(url, config);
            if (!response.ok) {
                let errorData;
                try {
                    errorData = await response.json();
                } catch (e) {
                    errorData = { message: `HTTP Error ${response.status}: ${response.statusText}` };
                }
                throw new Error(errorData.message || "An unexpected API error occurred.");
            }
            if (response.status === 24) return null;
            return await response.json();
        } catch (error) {
            console.error(`API Error [${endpoint}]:`, error);
            throw error;
        }
    },

    // Accounts
    getAccounts() {
        return this.request("/accounts");
    },
    getAccount(id) {
        return this.request(`/accounts/${id}`);
    },
    getAccountsByCustomer(customerId) {
        return this.request(`/accounts/customer/${customerId}`);
    },
    deposit(accountNumber, amount, description) {
        return this.request(`/accounts/${accountNumber}/deposit`, {
            method: "POST",
            body: JSON.stringify({ amount, description })
        });
    },
    withdraw(accountNumber, amount, description) {
        return this.request(`/accounts/${accountNumber}/withdraw`, {
            method: "POST",
            body: JSON.stringify({ amount, description })
        });
    },
    transfer(fromAccountId, toAccountId, amount, description) {
        return this.request("/transfer", {
            method: "POST",
            body: JSON.stringify({ fromAccountId, toAccountId, amount, description })
        });
    },

    // Transactions
    getTransactions(queryParams = "") {
        return this.request(`/transactions${queryParams}`);
    },
    getTransactionsByAccount(accountId) {
        return this.request(`/transactions/account/${accountId}`);
    },
    getSuspiciousTransactions() {
        return this.request("/transactions/suspicious");
    },

    // Savings Goals
    getGoals() {
        return this.request("/smart/goals");
    },
    createGoal(goal) {
        return this.request("/smart/goals", {
            method: "POST",
            body: JSON.stringify(goal)
        });
    },
    addGoalDeposit(id, amount) {
        return this.request(`/smart/goals/${id}/deposit?amount=${amount}`, {
            method: "PUT"
        });
    },

    // Spending Limits
    setSpendingLimit(limitData) {
        return this.request("/smart/limits", {
            method: "POST",
            body: JSON.stringify(limitData)
        });
    },
    getSpendingLimitStatus(accountNumber) {
        return this.request(`/smart/limits/${accountNumber}`);
    },

    // Scheduled Payments
    getScheduledPayments() {
        return this.request("/smart/scheduled");
    },
    schedulePayment(payment) {
        return this.request("/smart/scheduled", {
            method: "POST",
            body: JSON.stringify(payment)
        });
    },
    cancelScheduledPayment(id) {
        return this.request(`/smart/scheduled/${id}`, {
            method: "DELETE"
        });
    },

    // Loan & Health
    calculateLoan(loanReq) {
        return this.request("/smart/loan/calculate", {
            method: "POST",
            body: JSON.stringify(loanReq)
        });
    },
    calculateHealth(healthReq) {
        return this.request("/smart/health/calculate", {
            method: "POST",
            body: JSON.stringify(healthReq)
        });
    }
};

// UI Helper Notification Toasts
function showToast(message, type = "success") {
    let container = document.getElementById("toast-container");
    if (!container) {
        container = document.createElement("div");
        container.id = "toast-container";
        container.style.cssText = "position: fixed; top: 20px; right: 20px; z-index: 9999; display: flex; flex-direction: column; gap: 10px;";
        document.body.appendChild(container);
    }
    const toast = document.createElement("div");
    const bgColor = type === "success" ? "#16a34a" : type === "danger" ? "#dc2626" : "#2563eb";
    toast.style.cssText = `background: ${bgColor}; color: white; padding: 12px 20px; border-radius: 8px; font-weight: 500; font-size: 0.9rem; box-shadow: 0 4px 12px rgba(0,0,0,0.15); transition: opacity 0.3s ease;`;
    toast.innerText = message;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = "0";
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}
