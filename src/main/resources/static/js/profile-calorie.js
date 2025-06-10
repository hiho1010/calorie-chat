// /static/js/profile-calorie.js
document.addEventListener("DOMContentLoaded", () => {
  const meta    = document.getElementById("__meta");
  const userId  = meta.dataset.userid;
  const targetEl = document.getElementById("targetCalText");

  fetch(`/api/profile/target-calories?userId=${userId}`)
  .then(res => {
    if (!res.ok) throw new Error("API 호출 실패");
    return res.json();
  })
  .then(data => {
    // 소수점은 반올림해서 보여주기
    const kcal = Math.round(data.targetCalories);
    targetEl.textContent = `${kcal.toLocaleString()}`;
  })
  .catch(err => {
    console.error(err);
    targetEl.textContent = "불러오기 실패";
    targetEl.classList.replace("fw-bold", "text-danger");
  });
});