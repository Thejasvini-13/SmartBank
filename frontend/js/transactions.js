/* =========================================================
   SmartBank Transactions Page Logic
   ========================================================= */

document.addEventListener("DOMContentLoaded", () => {
    loadTransactions();

    document.getElementById("btn-apply-filters")?.addEventListener("click", () => {
        applyFilters();
    });

    document.getElementById("btn-reset-filters")?.addEventListener("click", () => {
        document.getElementById("filter-acc").value = "";
        document.getElementById("filter-category").value = "";
        document.getElementById("filter-type").value = "";
        document.getElementById("filter-min-amount").value = "";
        document.getElementById("filter-max-amount").value = "";
        loadTransactions();
    });
});

async function loadTransactions(query = "") {
    const body = document.getElementById("transactions-table-body");
    if (!body) return;

    try {
        const list = await API.getTransactions(query);
        body.innerHTML = "";

        if (list.length === 0) {
            body.innerHTML = "<tr><td colspan='7' style='text-align:center;'>No matching transactions found.</td></tr>";
            return;
        }

        list.forEach(tx => {
            const row = document.createElement("tr");
            const isCredit = tx.type === "CREDIT";

            row.innerHTML = `
                <td>#TX-${tx.transactionId}</td>
                <td><strong>${tx.accountNumber}</strong></td>
                <td><span class="badge ${isCredit ? 'badge-success' : 'badge-danger'}">${tx.type}</span></td>
                <td><span class="badge badge-info">${tx.category}</span></td>
                <td>${tx.description || '-'}</td>
                <td>${tx.timestamp.replace("T", " ").substring(0, 19)}</td>
                <td class="${isCredit ? 'text-credit' : 'text-debit'}">${isCredit ? '+' : '-'}₹${tx.amount.toLocaleString('en-IN')}</td>
            `;
            body.appendChild(row);
        });
    } catch (err) {
        showToast("Error loading transaction history: " + err.message, "danger");
    }
}

function applyFilters() {
    const acc = document.getElementById("filter-acc").value.trim();
    const cat = document.getElementById("filter-category").value;
    const type = document.getElementById("filter-type").value;
    const min = document.getElementById("filter-min-amount").value;
    const max = document.getElementById("filter-max-amount").value;

    const params = new URLSearchParams();
    if (acc) params.append("accountNumber", acc);
    if (cat) params.append("category", cat);
    if (type) params.append("type", type);
    if (min) params.append("minAmount", min);
    if (max) params.append("maxAmount", max);

    const queryStr = params.toString() ? `?${params.toString()}` : "";
    loadTransactions(queryStr);
}
