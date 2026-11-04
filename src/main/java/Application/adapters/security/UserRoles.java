package Application.adapters.security;

import Application.domain.model.user.Administrator;
import Application.domain.model.user.Buyer;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.model.user.Seller;
import Application.domain.model.user.Supervisor;
import Application.domain.model.user.User;

// un solo lugar que decide el nombre de rol de seguridad (para el claim
// "role" del JWT y para el authority de Spring Security) a partir del
// TIPO del usuario de dominio. no es User.getRoleName(): ese es para
// mostrarle algo al usuario ("Logistics Operator", con espacio y
// mayuscula inicial nomas); esto es para hasRole(...)/claims (sin
// espacios, todo mayusculas). lo usan JwtServiceAdapter (al generar el
// token) y JwtAuthenticationFilter (al reconstruir la autenticacion a
// partir del usuario recien cargado de la base de datos, no del claim
// del token: ver el comentario de JwtAuthenticationFilter)
final class UserRoles {

    private UserRoles() {
    }

    static String of(User user) {
        if (user instanceof Administrator) {
            return "ADMINISTRATOR";
        }
        if (user instanceof Supervisor) {
            return "SUPERVISOR";
        }
        if (user instanceof Seller) {
            return "SELLER";
        }
        if (user instanceof LogisticsOperator) {
            return "LOGISTICS_OPERATOR";
        }
        if (user instanceof Buyer) {
            return "BUYER";
        }
        throw new IllegalStateException("tipo de usuario sin rol de seguridad definido: " + user.getClass());
    }
}
