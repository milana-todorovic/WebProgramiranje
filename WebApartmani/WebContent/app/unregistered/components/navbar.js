Vue.component("unregistered-navbar",{
    data: function(){
        return{
            flag:0
        }
    },
    template:`
    <div>
        <b-navbar toggleable="lg" type="dark" variant="info">
            <b-navbar-brand href="#">WebApartmani</b-navbar-brand>
  
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
  
            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item href="#">
                        <router-link to="/apartments" exact>
                            Apartmani
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/register" exact>
                            Registracija
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/login" exact>
                            Logovanje
                        </router-link>
                    </b-nav-item>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
    </div>
    `
});