import { useAuth } from "../context/Authentication";
import { Navigate, useNavigate } from "react-router-dom";

interface ProtectedRouteProps {
    children: React.ReactNode;
    allowedRoles: string[];
}

const ProtectedRoute = ({ children, allowedRoles }: ProtectedRouteProps) => {
    const { user, loading } = useAuth();
    const navigate = useNavigate();

    if (loading) {
        return <div>Loading...</div>; // Puoi sostituire con un componente di loading
    }

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (!allowedRoles.includes(user.role)) {
        return <Navigate to="/unauthorized" replace />;
    }

    return <>{children}</>;
};

export default ProtectedRoute;
