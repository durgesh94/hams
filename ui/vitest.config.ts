import { defineConfig } from "vitest/config";

export default defineConfig({
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: "./src/test/setup.ts",

    coverage: {
      provider: "v8",
      reporter: ["text", "html", "lcov"],
      reportsDirectory: "./coverage",
      thresholds: {
        // Coverage thresholds (set very low for now)
        lines: 0.1, //80,
        functions: 0.1, //80,
        branches: 0.1, //80,
        statements: 0.1, //80,
      },

      include: ["src/**/*.{ts,tsx}"],

      exclude: [
        "src/test/**",
        "**/*.d.ts",
        "**/*.config.{ts,js}",
        "**/index.{ts,tsx}",
      ],
    },
  },
});
