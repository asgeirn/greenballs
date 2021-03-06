package hudson.plugins.greenballs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Asgeir Storesund Nilsen
 */
public class GreenBallFilter implements Filter {

    final Pattern pattern = Pattern.compile(".+/(\\d{2}x\\d{2})/blue(_anime|)\\.gif");
    final Logger logger = Logger.getLogger("hudson.plugins.greenballs");

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) req;
            final HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            final String uri = httpServletRequest.getRequestURI();
            if (uri.endsWith(".gif")) {
                final Matcher m = pattern.matcher(uri);
                if (m.matches()) {
                    if (logger.isLoggable(Level.FINE))
                        logger.log(
                                Level.FINE,
                                "Redirecting {0} to {1}",
                                new Object[] {
                                        uri,
                                        httpServletRequest.getContextPath() + "/plugin/greenballs/" + m.group(1)
                                                + "/green" + m.group(2) + ".gif" });
                    httpServletResponse.setHeader("Cache-Control", "public, s-maxage=86400");
                    httpServletResponse.setDateHeader("Expires", System.currentTimeMillis() + 86400000);
                    RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher(httpServletRequest
                            .getContextPath() + "/plugin/greenballs/" + m.group(1) + "/green" + m.group(2) + ".gif");
                    dispatcher.forward(httpServletRequest, httpServletResponse);
                }
            }
        }
        chain.doFilter(req, resp);
    }

    public void destroy() {
    }
}
