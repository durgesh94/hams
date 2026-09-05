import { Logout, Menu } from "@mui/icons-material";
import {
  AppBar,
  Avatar,
  Box,
  IconButton,
  Toolbar,
  Typography,
} from "@mui/material";

import { drawerWidth } from "../../constants";

interface HeaderProps {
  onMenuClick: () => void;
}

const Header = ({ onMenuClick }: HeaderProps) => {
  // Temporary values.
  // Later these will come from AuthContext.
  const username = "Admin";
  const role = "ADMIN";

  return (
    <AppBar
      position="fixed"
      sx={{
        width: { md: `calc(100% - ${drawerWidth}px)` },
        ml: { md: `${drawerWidth}px` },
      }}
    >
      <Toolbar>
        <IconButton
          color="inherit"
          edge="start"
          onClick={onMenuClick}
          sx={{ mr: 2, display: { md: "none" } }}
          aria-label="open navigation"
        >
          <Menu />
        </IconButton>

        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Hospital Appointment Management System
        </Typography>

        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 1,
          }}
        >
          <Avatar sx={{ width: 32, height: 32 }}>{username.charAt(0)}</Avatar>

          <Box sx={{ display: { xs: "none", sm: "block" } }}>
            <Typography variant="body2">{username}</Typography>

            <Typography variant="caption">{role}</Typography>
          </Box>

          <IconButton color="inherit" aria-label="logout">
            <Logout />
          </IconButton>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
