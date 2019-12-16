package com.xebia.fs101.writerpad.security;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private AuthenticationManager authenticationManager;
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request,
//                                                HttpServletResponse response
//    ) throws AuthenticationException {
//
//        try {
//            User creds = new ObjectMapper().readValue(request.getInputStream(),
//                                                      User.class);
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(creds.getUsername(),
//                                                            creds.getPassword(),
//                                                            new ArrayList<>()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult
//    ) throws IOException, ServletException {
//
//        String token = Jwts.builder()
//                .setSubject(((User) authResult.getPrincipal()).getUsername())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS256, SECRET)
//                .compact();
//        System.out.println(token);
//        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
//    }
}
