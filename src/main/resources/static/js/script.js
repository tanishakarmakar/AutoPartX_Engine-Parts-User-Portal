document.addEventListener("DOMContentLoaded", () => {
    const cards = document.querySelectorAll(".apx-feature-card");

    cards.forEach((card, index) => {
        card.style.opacity = "0";
        card.style.transform = "translateY(20px)";
        card.style.transition = "opacity 0.6s ease, transform 0.6s ease";
        setTimeout(() => {
            card.style.opacity = "1";
            card.style.transform = "translateY(0)";
        }, 300 * index); // Stagger effect
    });
});
