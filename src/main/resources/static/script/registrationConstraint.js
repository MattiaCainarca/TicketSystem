document.addEventListener("DOMContentLoaded", function () {
    const firstNameInput = document.getElementById("firstName");
    const lastNameInput = document.getElementById("lastName");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const passwordConfirmInput = document.getElementById("passwordConfirm");
    const form = document.querySelector("form");

    const nameRegex = /^[a-zA-Z]+$/;
    const usernameRegex = /^[a-zA-Z0-9_]+$/;
    const passwordRegex = /^[a-zA-Z0-9_]{8,15}$/;

    function validateField(input, regex) {
        const isValid = regex.test(input.value);
        input.classList.toggle("is-valid", isValid);
        input.classList.toggle("is-invalid", !isValid);
        return isValid;
    }

    function validatePasswordsMatch() {
        const doPasswordsMatch = passwordInput.value === passwordConfirmInput.value;
        passwordConfirmInput.classList.toggle("is-valid", doPasswordsMatch);
        passwordConfirmInput.classList.toggle("is-invalid", !doPasswordsMatch);
        return doPasswordsMatch;
    }

    function updateSubmitButton() {
        const allValid =
            validateField(firstNameInput, nameRegex) &&
            validateField(lastNameInput, nameRegex) &&
            validateField(usernameInput, usernameRegex) &&
            validateField(passwordInput, passwordRegex) &&
            validatePasswordsMatch();

        let submitButton = form.querySelector("button[type='submit']");

        if (allValid) {
            if (!submitButton) {
                submitButton = document.createElement("button");
                submitButton.type = "submit";
                submitButton.classList.add("btn", "btn-primary", "fs-5", "mt-3");
                submitButton.textContent = "Register";
                form.appendChild(submitButton);
            }
        } else if (submitButton) {
            submitButton.remove();
        }
    }

    [firstNameInput, lastNameInput, usernameInput, passwordInput, passwordConfirmInput].forEach((input) => {
        input.addEventListener("input", () => {
            if (input === passwordConfirmInput) {
                validatePasswordsMatch();
            } else {
                const regex =
                    input === usernameInput
                        ? usernameRegex
                        : input === passwordInput
                            ? passwordRegex
                            : nameRegex;
                validateField(input, regex);
            }
            updateSubmitButton();
        });
    });
});
