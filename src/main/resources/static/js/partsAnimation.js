document.addEventListener('DOMContentLoaded', function() {
    // Intersection Observer for scroll animations
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate__fadeInUp');
            }
        });
    }, { threshold: 0.1 });

    document.querySelectorAll('.apx-part-card').forEach(card => {
        observer.observe(card);
    });

    // Close alert button
    document.querySelector('.apx-alert-close')?.addEventListener('click', function() {
        this.closest('.apx-alert').style.opacity = '0';
        setTimeout(() => {
            this.closest('.apx-alert').remove();
        }, 300);
    });
});