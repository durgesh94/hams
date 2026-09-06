import type { ReactNode } from "react";
import { Provider } from "react-redux";
import { CssBaseline, ThemeProvider } from "@mui/material";

import { store } from "../store";
import theme from "../../theme/theme";
import AuthInitializer from "../../features/auth/AuthInitializer";

interface AppProvidersProps {
  children: ReactNode;
}

const AppProviders = ({ children }: AppProvidersProps) => {
  return (
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthInitializer>{children}</AuthInitializer>
      </ThemeProvider>
    </Provider>
  );
};

export default AppProviders;
