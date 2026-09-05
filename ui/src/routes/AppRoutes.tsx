import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";
import Dashboard from "../pages/Dashboard";
import Login from "../pages/Login";
import AppLayout from "../components/layout/AppLayout";
import Appointment from "../pages/Appointment";
import Doctor from "../pages/Doctor";
import Patient from "../pages/Patient";

const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={<Login />} />
        {/* Private Routes */}
        <Route element={<AppLayout />}>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/doctors" element={<Doctor />} />
          <Route path="/patients" element={<Patient />} />
          <Route path="/appointments" element={<Appointment />} />
        </Route>
        {/* Fallback */}
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
