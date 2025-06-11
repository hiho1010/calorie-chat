// /static/js/weight-register.js
document.addEventListener("DOMContentLoaded", () => {
  const form     = document.getElementById("weightForm");
  const meta     = document.getElementById("__meta");
  const modalEl  = document.getElementById("weightModal");
  const modal    = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);

  form.addEventListener("submit", e => {
    e.preventDefault();

    const userId = meta.dataset.userid;
    const date   = document.getElementById("weightDate").value;
    const weight = parseFloat(document.getElementById("weightValue").value);

    fetch("/api/weight-log", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, date, weight })
    })
        .then(res => {
          if (!res.ok) throw new Error("저장에 실패했습니다.");

          // 본문이 없으므로 json 파싱 안 함
          return null;
        })
        .then(() => {
          if (window.weightChart && window.weightChart.data) {
            window.weightChart.data.labels.push(new Date(date).toLocaleDateString());
            window.weightChart.data.datasets[0].data.push(weight);
            window.weightChart.update();
          }
          modal.hide();
          form.reset();
        })
        .catch(err => {
          console.error(err);
         // alert(err.message);
        });
  });
});