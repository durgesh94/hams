import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: {
      main: "#1976D2",
    },
    secondary: {
      main: "#00897B",
    },
    error: {
      main: "#D32F2F",
    },
    warning: {
      main: "#FFA000",
    },
    info: {
      main: "#1976D2",
    },
    success: {
      main: "#388E3C",
    },
    background: {
      default: "#F5F9FC",
      paper: "#FFFFFF",
    },

    text: {
      primary: "#263238",
      secondary: "#546E7A",
    },
  },
  typography: {
    fontFamily: "Roboto, Arial, sans-serif",
  },
  shape: {
    borderRadius: 8,
  },
});

export default theme;
