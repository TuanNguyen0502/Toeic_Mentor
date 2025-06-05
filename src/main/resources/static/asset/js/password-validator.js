document.addEventListener("DOMContentLoaded", function() {
    // Get the password and confirmPassword elements
    const passwordInput = document.getElementById("password");
    const confirmPasswordInput = document.getElementById("confirmPassword");
    const form = document.querySelector(".custom-form");

    // Find the container div for confirmPassword field
    const confirmPasswordContainer = confirmPasswordInput.closest(".col-lg-12");

    // Find existing error span or create a new one
    let errorSpan = confirmPasswordContainer.querySelector(".text-danger");
    if (!errorSpan) {
        errorSpan = document.createElement("span");
        errorSpan.classList.add("text-danger");
        confirmPasswordContainer.insertBefore(errorSpan, confirmPasswordContainer.firstChild);
    }

    // Store original error message if it exists
    const originalErrorMessage = errorSpan.textContent;

    // Function to check if passwords match
    function checkPasswordMatch() {
        if (confirmPasswordInput.value === "") {
            errorSpan.style.display = "none";
            return true;
        }

        if (passwordInput.value !== confirmPasswordInput.value) {
            errorSpan.textContent = "Passwords do not match";
            errorSpan.style.display = "block";
            return false;
        } else {
            errorSpan.style.display = "none";
            return true;
        }
    }

    // Check passwords on input change
    confirmPasswordInput.addEventListener("input", checkPasswordMatch);
    passwordInput.addEventListener("input", function() {
        if (confirmPasswordInput.value !== "") {
            checkPasswordMatch();
        }
    });

    // Prevent form submission if passwords don't match
    form.addEventListener("submit", function(event) {
        if (!checkPasswordMatch() && confirmPasswordInput.value !== "") {
            event.preventDefault();
        }
    });
});
