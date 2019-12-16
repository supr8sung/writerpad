package com.xebia.fs101.writerpad.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    //    @Autowired
//    private UserRepository userRepository;
//
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {

        super(authenticationManager);
    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        String header = request.getHeader(HEADER_STRING);
//        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
//            chain.doFilter(request, response);
//            return;
//        }
//        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(
//                request);
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        chain.doFilter(request, response);
//    }
//
//    private UsernamePasswordAuthenticationToken getAuthentication(
//            HttpServletRequest request) {
//
//        String token = request.getHeader(HEADER_STRING);
//        if (token != null) {
//            String username = Jwts.parser()
//                    .setSigningKey(SECRET)
//                    .parseClaimsJws(
//                            token.replace(TOKEN_PREFIX, "")
//                    )
//                    .getBody()
//                    .getSubject();
//            if (username != null) {
//                User user = userRepository.findByUsername(username);
//                return new UsernamePasswordAuthenticationToken(username, null,
//                                                               new ArrayList<>());
//            }
//            return null;
//        }
//        return null;
//    }
}
