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
        // Coverage thresholds (set 50% for now)
        lines: 60,
        functions: 60,
        branches: 60,
        statements: 60,
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
