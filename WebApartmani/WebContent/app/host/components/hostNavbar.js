Vue.component("host-navbar",{
    data: function(){
        return{
            flag:0
        }
    },
    template:`
    <div>
        <b-navbar toggleable="lg" type="dark" variant="info">
            <b-navbar-brand href="#">BookWebApy</b-navbar-brand>
  
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
  
            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item href="#">
                        <router-link to="/apartments" exact>
                            Moji apartmani
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/reservations" exact>
                            Rezervacije
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/users" exact>
                            Moji gosti
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/addApartment" exact>
                            Dodaj apartman
                        </router-link>
                    </b-nav-item>
                </b-navbar-nav>
  
                <b-navbar-nav class="ml-auto">
                
                    <b-nav-item-dropdown right>
            
                    <template v-slot:button-content>
                        <em>Korisnik</em>
                    </template>
                    <b-dropdown-item  href="#">
                        Profil
                    </b-dropdown-item>
                    <b-dropdown-item href="#" @click="logout">
                        Odjavi se
                    </b-dropdown-item>

                    </b-nav-item-dropdown>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
    </div>
    `,
    methods:{
        logout: function(event){
            event.preventDefault;
            alert("Simulacija odjave");
        }
    }

});