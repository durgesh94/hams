import { useState } from "react";
import { Outlet } from "react-router-dom";
import { Box, Toolbar } from "@mui/material";

import Header from "./Header";
import Sidebar from "./Sidebar";
import { drawerWidth } from "../../constants";

const AppLayout = () => {
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleMenuClick = () => {
    setMobileOpen((previous) => !previous);
  };

  const handleDrawerClose = () => {
    setMobileOpen(false);
  };

  return (
    <Box sx={{ display: "flex" }}>
      <Header onMenuClick={handleMenuClick} />

      <Sidebar mobileOpen={mobileOpen} onClose={handleDrawerClose} />

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: {
            xs: "100%",
            md: `calc(100% - ${drawerWidth}px)`,
          },
          ml: {
            xs: 0,
            md: `${drawerWidth}px`,
          },
          minHeight: "100vh",
          backgroundColor: "background.default",
          p: 3,
          boxSizing: "border-box",
        }}
      >
        <Toolbar />

        <Outlet />
      </Box>
    </Box>
  );
};

export default AppLayout;
