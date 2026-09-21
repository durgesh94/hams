import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

describe("Testing setup", () => {
  it("should render a React component", () => {
    render(<h1>HAMS Testing</h1>);

    expect(
      screen.getByRole("heading", {
        name: "HAMS Testing",
      }),
    ).toBeInTheDocument();
  });
});

if (typeof SVGElement !== "undefined") {
  SVGElement.prototype.focus = () => {};
}
