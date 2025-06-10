document.addEventListener("DOMContentLoaded", () => {
  const userId = document.getElementById("__meta")?.dataset.userid;
  if (!userId) return;

  const form = document.getElementById("mealForm");
  const container = document.getElementById("foodItemsContainer");
  const addBtn = document.getElementById("addFoodItemBtn");
  const mealModalEl = document.getElementById("mealModal");

  const MAX_ITEMS = 5;

  // 초기 1개 음식 항목
  container.innerHTML = renderFoodItem();
  updateRemoveButtons();

  // 음식 항목 추가
  addBtn.addEventListener("click", () => {
    const count = container.querySelectorAll(".food-item").length;
    if (count >= MAX_ITEMS) {
      alert("최대 5개의 음식을 등록할 수 있습니다.");
      return;
    }
    container.insertAdjacentHTML("beforeend", renderFoodItem());
    updateRemoveButtons();
  });

  // 음식 항목 삭제
  container.addEventListener("click", (e) => {
    if (e.target.classList.contains("remove-food-btn")) {
      const item = e.target.closest(".food-item");
      if (item) item.remove();
      updateRemoveButtons();
    }
  });

  // 등록 처리
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const mealTime = document.getElementById("mealTime").value;
    const eatenAt = document.getElementById("eatenAt").value;

    const foodItems = Array.from(container.querySelectorAll(".food-item")).map(el => {
      const [name, calories, quantity] = el.querySelectorAll("input");
      return {
        name: name.value,
        calories: parseFloat(calories.value),
        quantity: quantity.value
      };
    });

    const totalCalories = foodItems.reduce((sum, item) => sum + item.calories, 0);

    try {
      const res = await fetch(`/api/users/${userId}/meals`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ mealTime, eatenAt, totalCalories, foodItems })
      });

      if (!res.ok) throw new Error("등록 실패");

      const data = await res.json();
      alert(`식단 등록 완료! (mealId: ${data.mealId})`);
      form.reset();
      container.innerHTML = renderFoodItem();
      updateRemoveButtons();
      bootstrap.Modal.getInstance(mealModalEl).hide();
    } catch (err) {
      alert("오류 발생: " + err.message);
    }
  });

  // 모달 닫힐 때 초기화
  mealModalEl.addEventListener("hidden.bs.modal", () => {
    form.reset();
    container.innerHTML = renderFoodItem();
    updateRemoveButtons();
  });

  // 항목 HTML
  function renderFoodItem() {
    return `
      <div class="food-item mb-2 d-flex gap-2 align-items-start">
        <div class="flex-grow-1">
          <input type="text" class="form-control mb-1" placeholder="음식 이름" name="name" required />
          <input type="number" class="form-control mb-1" placeholder="칼로리(kcal)" name="calories" required />
          <input type="text" class="form-control mb-1" placeholder="수량(예: 1그릇)" name="quantity" required />
        </div>
        <button type="button" class="btn btn-outline-danger btn-sm remove-food-btn mt-1">❌</button>
      </div>
    `;
  }

  // 삭제 버튼 제어 (1개만 남으면 비활성화)
  function updateRemoveButtons() {
    const allItems = container.querySelectorAll(".food-item");
    const allButtons = container.querySelectorAll(".remove-food-btn");

    if (allItems.length <= 1) {
      allButtons.forEach(btn => btn.disabled = true);
    } else {
      allButtons.forEach(btn => btn.disabled = false);
    }
  }
});