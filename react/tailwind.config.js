// tailwind.config.js
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Inter", "system-ui", "-apple-system", "sans-serif"],
      },
      fontSize: {
        "8xl": "8rem",
        "7xl": "4rem",
      },
      colors: {
        primary: {
          100: "#FFFFFF",
        },
        surface: "#E2E2E2",
        card: {
          pdf: "#D95F5F",
          txt: "#94C4E6",
          plain: "#D9D9D9",
        },
        text: {
          default: "#000000",
          muted: "#e3e3e3",
          positive: "#16a34a",
          negative: "#dc2626",
        },
      },
    },
  },
  plugins: [],
};
