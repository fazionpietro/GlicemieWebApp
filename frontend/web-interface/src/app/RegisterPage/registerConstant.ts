/** Available user roles for registration. */
export const data = ["Paziente", "Medico"];

/** Password strength requirements regex and labels. */
export const requirements = [
    { re: /[0-9]/, label: "Include numeri" },
    { re: /[a-z]/, label: "Include lettere minuscole" },
    { re: /[A-Z]/, label: "Include lettere maiuscole" },
    { re: /[$&+,:;=?@#|'<>.^*()%!-]/, label: "Include simboli speciali" },
];