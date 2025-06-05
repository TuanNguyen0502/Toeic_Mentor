document.addEventListener("DOMContentLoaded", function() {
    // Get the password input element
    const passwordInput = document.getElementById("password");

    // Get the confirm password input element (handle both ID variations)
    let confirmPasswordInput = document.getElementById("confirmPassword");
    if (!confirmPasswordInput) {
        confirmPasswordInput = document.getElementById("confirm_password");
    }

    // If both password fields exist, set up validation
    if (passwordInput && confirmPasswordInput) {
        const form = passwordInput.closest("form");

        // Find the container for confirm password field
        const confirmPasswordParent = confirmPasswordInput.closest(".form-floating").parentElement;

        // Find existing error span or get the one right before the form-floating
        let errorSpan = confirmPasswordParent.querySelector(".text-danger");
        if (!errorSpan) {
            // If no error span exists, create one
            errorSpan = document.createElement("span");
            errorSpan.classList.add("text-danger");
            confirmPasswordParent.insertBefore(errorSpan, confirmPasswordInput.closest(".form-floating"));
        }

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
        if (form) {
            form.addEventListener("submit", function(event) {
                if (!checkPasswordMatch() && confirmPasswordInput.value !== "") {
                    event.preventDefault();
                }
            });
        }
    }
});
