package SpringSecurityApp.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;

	/*
	@Override
	protected void configure(HttpSecurity web)throws Exception{

		web.formLogin().loginPage("/login").defaultSuccessUrl("/item/top").failureUrl("/login-error").permitAll();
		web.authorizeRequests().antMatchers("/css/**", "/images/**", "/js/**","/user/new","/user/create").permitAll().anyRequest().authenticated();
		web.logout().logoutSuccessUrl("/login").permitAll();
	}
*/
    /**
     * 認可設定を無視するリクエストを設定
     * 静的リソース(image,javascript,css)を認可処理の対象から除外する
     */
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**","/user/new","/user/create");
	}

    /**
     * 認証・認可の情報を設定する
     * 画面遷移のURL・パラメータを取得するname属性の値を設定
     * SpringSecurityのconfigureメソッドをオーバーライドしています。
     */

	@Override
	protected void configure(HttpSecurity http) throws Exception{
        http
        .authorizeRequests()
        	.antMatchers("/user/new","/user/create").permitAll()//認証なしでアクセス可能なパス
            .anyRequest().authenticated()//上記以外は全て認証無しの場合アクセス不許可
            .and()
        .formLogin()
            .loginPage("/login") //ログインページはコントローラを経由しないのでViewNameとの紐付けが必要
            .defaultSuccessUrl("/item/top", true) //認証が成功した際に遷移するURL
            .failureUrl("/login-error") //認証が失敗した際に遷移するURL
            .permitAll() //どのユーザでも接続できる。
            .and()
        .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll();
}

	/**
     * 認証時に利用するデータソースを定義する設定メソッド
     * ここではDBから取得したユーザ情報をuserDetailsServiceへセットすることで認証時の比較情報としている
     * @param auth
     * @throws Exception
     * AuthenticationManagerBuilderは認証系の機能を有している。
     * userDetailsServiceもその一つでフォームに入力されたユーザが使用可能か判断します。
     * https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/config/annotation/authentication/builders/AuthenticationManagerBuilder.html
     */
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception{

		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
