import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import Card from "./Card";

describe("Card", () => {
  it("should render title, count and description", () => {
    render(
      <Card
        title="Total Doctors"
        count={25}
        description="Registered doctors"
      />,
    );

    expect(
      screen.getByRole("heading", {
        name: "Total Doctors",
      }),
    ).toBeInTheDocument();

    expect(screen.getByText("25")).toBeInTheDocument();

    expect(screen.getByText("Registered doctors")).toBeInTheDocument();
  });

  it("should render title and count without description", () => {
    render(<Card title="Total Patients" count={100} />);

    expect(
      screen.getByRole("heading", {
        name: "Total Patients",
      }),
    ).toBeInTheDocument();

    expect(screen.getByText("100")).toBeInTheDocument();

    expect(screen.queryByText("Registered doctors")).not.toBeInTheDocument();
  });
});
