import { useState } from "react";
import { Visibility, VisibilityOff, LocalHospital } from "@mui/icons-material";
import { getApiErrorMessage } from "../../features/api/apiError";
import {
  Alert,
  Box,
  Button,
  IconButton,
  InputAdornment,
  Paper,
  TextField,
  Typography,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

import { useAppDispatch } from "../../app/hooks";
import { setCredentials } from "../../features/auth/authSlice";
import { useLoginMutation } from "../../features/auth/authApi";

const Login = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const [login, { isLoading }] = useLoginMutation();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    setError("");

    if (!username.trim() || !password.trim()) {
      setError("Please enter username and password.");
      return;
    }

    try {
      const response = await login({
        username: username.trim(),
        password,
      }).unwrap();

      dispatch(
        setCredentials({
          token: response.token,
          user: {
            username: "",
            role: "",
          },
        }),
      );

      navigate("/dashboard", {
        replace: true,
      });
    } catch (error) {
      setError(getApiErrorMessage(error));
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "background.default",
        px: 2,
      }}
    >
      <Paper
        elevation={0}
        sx={{
          width: "100%",
          maxWidth: 420,
          p: { xs: 3, sm: 4 },
          border: "1px solid",
          borderColor: "divider",
          borderRadius: 2,
        }}
      >
        {/* Logo */}
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            mb: 2,
          }}
        >
          <Box
            sx={{
              width: 64,
              height: 64,
              borderRadius: "50%",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              backgroundColor: "primary.main",
              color: "primary.contrastText",
            }}
          >
            <LocalHospital fontSize="large" />
          </Box>
        </Box>

        {/* Title */}
        <Box sx={{ textAlign: "center", mb: 4 }}>
          <Typography variant="h5" color="text.primary">
            Hospital
          </Typography>

          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
            Appointment Management System
          </Typography>
        </Box>

        {/* Login heading */}
        <Typography variant="h6" sx={{ mb: 0.5 }}>
          Welcome Back
        </Typography>

        <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
          Please login to continue
        </Typography>

        {/* Error */}
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {/* Form */}
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            fullWidth
            label="Username"
            placeholder="Enter username"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            autoComplete="username"
            disabled={isLoading}
            sx={{ mb: 2 }}
          />

          <TextField
            fullWidth
            label="Password"
            placeholder="Enter password"
            type={showPassword ? "text" : "password"}
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            autoComplete="current-password"
            disabled={isLoading}
            slotProps={{
              input: {
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPassword((previous) => !previous)}
                      edge="end"
                      disabled={isLoading}
                      aria-label={
                        showPassword ? "Hide password" : "Show password"
                      }
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              },
            }}
            sx={{ mb: 3 }}
          />

          <Button
            type="submit"
            variant="contained"
            fullWidth
            size="large"
            disabled={isLoading}
            sx={{
              py: 1.25,
              fontWeight: 600,
            }}
          >
            {isLoading ? "Logging in..." : "Login"}
          </Button>
        </Box>
      </Paper>
    </Box>
  );
};

export default Login;
