import { Add } from "@mui/icons-material";
import { Box, Button, Typography } from "@mui/material";

const PageHeader = ({
  title,
  subtitle,
  handleAction,
}: {
  title: string;
  subtitle?: string;
  handleAction: () => void;
}) => {
  return (
    <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: { xs: "flex-start", sm: "center" },
          flexDirection: { xs: "column", sm: "row" },
          gap: 2,
          mb: 3,
        }}
      >
        <Box>
          <Typography variant="h4">{title}</Typography>

          {subtitle && (
            <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
              {subtitle}
            </Typography>
          )}
        </Box>

        <Button variant="contained" startIcon={<Add />} onClick={handleAction}>
          Add Doctor
        </Button>
      </Box>
  );
};

export default PageHeader;
