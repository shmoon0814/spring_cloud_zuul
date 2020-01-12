package m.s.h.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RouteFilter extends ZuulFilter {

    @Value("${security.jwt.secret}")
    private String secret;
    private String prefix = "Bearer";

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String token = ctx.getRequest().getHeader("Authorization");
        token = token.replace(prefix + " ", "");
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();

        if(SecurityContextHolder.getContext() != null) {
            ctx.addZuulRequestHeader("username", username);
            ctx.addZuulRequestHeader("user_id", claims.get("user_id").toString());
        }
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public String filterType() {
//        return "route";
//    }
//
//    @Override
//    public int filterOrder() {
//        return -10000;
//    }
}
