Vue.component("admin-navbar",{
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
                        <router-link to="/amenities" exact>
                            Sadr\u017Eaji apartmana
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/reservations" exact>
                            Rezervacije
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/users" exact>
                            Korisnici
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/host" exact>
                            Dodaj doma\u0107ina
                        </router-link>
                    </b-nav-item>
                    <b-nav-item href="#">
                         <router-link to="/holidays" exact>
                            Praznici
                        </router-link>
                    </b-nav-item>
                </b-navbar-nav>
  
                <b-navbar-nav class="ml-auto">
                
                    <b-nav-item-dropdown right>
            
                    <template v-slot:button-content>
                        <em>Korisnik</em>
                    </template>
                    <b-dropdown-item  href="#" @click="profile">
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
            axios.post("/WebApartmani/rest/auth/logout").then(response =>
    		window.location.href = "http://localhost:8081/WebApartmani/").catch();
        },
        profile(){
        	router.push('profile');
        }
    }

});