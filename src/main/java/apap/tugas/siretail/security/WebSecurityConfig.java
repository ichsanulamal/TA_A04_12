package apap.tugas.siretail.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .csrf().disable() // untuk testing webservice POST. Kalo kelompok sebelah udah jadi dihapus
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/user/add").hasAnyAuthority("Kepala Retail")
                .antMatchers("cabang/**").hasAnyAuthority("Kepala Retail", "Manager Cabang")
                .antMatchers("cabang/update").hasAnyAuthority("Kepala Retail", "Manager Cabang")
                .antMatchers("cabang/update/**").hasAnyAuthority("Kepala Retail", "Manager Cabang")
                .antMatchers("cabang/delete/**").hasAnyAuthority("Kepala Retail", "Manager Cabang")
                .antMatchers("/cabang/permintaan-cabang").hasAnyAuthority("Kepala Retail")
                .antMatchers("/user/edit/**").hasAnyAuthority("Kepala Retail", "Manager Cabang")
                .antMatchers("/item/coupon/**").hasAnyAuthority("Kepala Retail", "Manager Cabang")
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll();
    }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication()
              .passwordEncoder(encoder())
              .withUser("kijangSatu").password(encoder().encode("nasiGoreng"))
              .authorities("Kepala Retail");
  }

    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
}