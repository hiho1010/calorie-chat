document.addEventListener("DOMContentLoaded", () => {
  const userId = document.getElementById("__meta")?.dataset.userid;
  if (!userId) return;

  fetch(`/api/weight-log?userId=${userId}`)
  .then(res => res.json())
  .then(data => {
    if (!data.length) {
      document.getElementById("weightChart").replaceWith("몸무게 기록이 없습니다.");
      return;
    }

    const labels = data.map(log => new Date(log.loggedAt).toLocaleDateString());
    const values = data.map(log => log.weight);

    const ctx = document.getElementById("weightChart").getContext("2d");
    new Chart(ctx, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: '몸무게 (kg)',
          data: values,
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 2,
          tension: 0.3,
          fill: false
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: true }
        },
        scales: {
          y: {
            title: { display: true, text: "kg" },
            beginAtZero: false
          }
        }
      }
    });
  })
  .catch(err => {
    console.error("차트 데이터를 불러오지 못했습니다", err);
  });
});