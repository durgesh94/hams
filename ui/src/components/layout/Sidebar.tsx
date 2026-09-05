import {
  CalendarMonth,
  Dashboard,
  LocalHospital,
  People,
  Person,
} from "@mui/icons-material";
import {
  Box,
  Divider,
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
} from "@mui/material";
import { NavLink } from "react-router-dom";
import { drawerWidth } from "../../constants";

const menuItems = [
  {
    label: "Dashboard",
    path: "/dashboard",
    icon: <Dashboard />,
  },
  {
    label: "Doctors",
    path: "/doctors",
    icon: <LocalHospital />,
  },
  {
    label: "Patients",
    path: "/patients",
    icon: <People />,
  },
  {
    label: "Appointments",
    path: "/appointments",
    icon: <CalendarMonth />,
  },
];

interface SidebarProps {
  mobileOpen: boolean;
  onClose: () => void;
}

const Sidebar = ({ mobileOpen, onClose }: SidebarProps) => {
  const drawerContent = (
    <Box>
      <Toolbar>
        <Person sx={{ mr: 1 }} />
        <Typography variant="h6">
          HAMS
        </Typography>
      </Toolbar>

      <Divider />

      <List>
        {menuItems.map((item) => (
          <ListItemButton
            key={item.path}
            component={NavLink}
            to={item.path}
            onClick={onClose}
            sx={{
              mx: 1,
              mb: 0.5,
              borderRadius: 1,
              "&.active": {
                backgroundColor: "primary.main",
                color: "primary.contrastText",
                "& .MuiListItemIcon-root": {
                  color: "inherit",
                },
              },
            }}
          >
            <ListItemIcon>{item.icon}</ListItemIcon>
            <ListItemText primary={item.label} />
          </ListItemButton>
        ))}
      </List>
    </Box>
  );

  return (
    <>
      {/* Desktop */}
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: "none", md: "block" },
          "& .MuiDrawer-paper": {
            width: drawerWidth,
            boxSizing: "border-box",
          },
        }}
        open
      >
        {drawerContent}
      </Drawer>

      {/* Mobile */}
      <Drawer
        variant="temporary"
        open={mobileOpen}
        onClose={onClose}
        sx={{
          display: { xs: "block", md: "none" },
          "& .MuiDrawer-paper": {
            width: drawerWidth,
          },
        }}
      >
        {drawerContent}
      </Drawer>
    </>
  );
};

export default Sidebar;
