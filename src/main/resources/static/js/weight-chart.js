// static/js/weight-chart.js
document.addEventListener('DOMContentLoaded', () => {

  const meta   = document.getElementById('__meta');
  const userId = meta?.dataset.userid;
  if (!userId) return;      // 비로그인 또는 세션 만료

  fetch(`/api/weight-log?userId=${userId}`)
  .then(r => r.ok ? r.json() : Promise.reject())
  .then(drawChart)
  .catch(() => console.warn('몸무게 데이터를 불러올 수 없습니다.'));
});

function drawChart(logs) {
  if (!logs?.length) return;

  // 날짜순 정렬(오름차순)
  logs.sort((a, b) => new Date(a.date) - new Date(b.date));

  const labels = logs.map(l => l.date);
  const data   = logs.map(l => l.weight);

  new Chart(
      document.getElementById('weightChart'),
      {
        type: 'line',
        data: {
          labels,
          datasets: [{
            label: '몸무게(kg)',
            data,
            tension: 0.35,
            fill: false
          }]
        }
      }
  );
}