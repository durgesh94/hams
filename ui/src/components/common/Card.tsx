import { Card as MuiCard, CardContent, Grid, Typography } from "@mui/material";

interface CardProps {
  title: string;
  count: number;
  description?: string;
}

const Card = ({ title, count, description }: CardProps) => {
  return (
    <Grid size={{ xs: 12, md: 4 }}>
      <MuiCard>
        <CardContent>
          <Typography variant="h6">{title}</Typography>
          <Typography variant="h3">{count}</Typography>
          {description && (
            <Typography variant="body2" color="text.secondary">
              {description}
            </Typography>
          )}
        </CardContent>
      </MuiCard>
    </Grid>
  );
};

export default Card;
