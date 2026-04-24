document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".modal-backdrop").forEach(el => el.remove());
    document.body.classList.remove("modal-open");
    document.body.style.overflow = "auto";
    document.body.style.paddingRight = "0";

    const overlay = document.querySelector(".loading-overlay");
    if (overlay) {
        overlay.classList.remove("show");
        overlay.style.display = "none";
    }

    document.querySelectorAll("form[data-loading='true']").forEach(form => {
        form.addEventListener("submit", function () {
            document.getElementById("loadingOverlay")?.classList.add("show");
        });
    });
});
