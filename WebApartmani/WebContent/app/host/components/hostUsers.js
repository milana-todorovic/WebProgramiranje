Vue.component("host-users",{
    data: function(){
        return{
            users:[],
            roles: [
                { value: null, text: 'Izaberite ulogu' },
                { value: 'Administrator', text: 'Administrator' },
                { value: 'Doma\u0107in', text: 'Doma\u0107in' },
                { value: 'Gost', text: 'Gost' },
              ],
            genders: [
                { value: null, text: 'Izaberite pol' },
                { value: 'mu\u0161ki', text: 'Mu\u0161ki' },
                { value: '\u017Eenski', text: '\u017Denski' },
                { value: 'drugi', text: 'Ostalo' },
              ],
            searchParam: {username:null, gender:null, role:null}
        }       
    },
    created() {
    	this.fetchUsers();
    },
    watch: {
        '$route': 'fetchUsers'
    },
    methods:{
        fetchUsers(){
        	this.searchParam.username = null;
        	this.searchParam.gender = null;
        	this.searchParam.role = null;
        	axios.get("/WebApartmani/rest/users").then(
        			response => this.users = response.data).catch(error => console.log(alert(error.response.data)));             
        },
        searchSubmit(event){
        	event.preventDefault();
        	axios.post("/WebApartmani/rest/users/search", this.searchParam).then(
        			response => this.users = response.data).catch(error => console.log(alert(error.response.data)));
        },
        searchReset(event){
        	event.preventDefault();
        	this.fetchUsers();
        }
    },
    template:`
            <b-container class="w-75">
                <b-row>
                    <b-col>
                    	<b-form class="p-1" inline v-on:submit="searchSubmit" v-on:reset="searchReset">
                          	<b-form-input class="m-1" placeholder="Korisni\u010Dko ime" v-model="searchParam.username"></b-form-input>
                                    
                            <b-form-select class="m-1" :options="genders" v-model="searchParam.gender"></b-form-select>
                                    
                            <b-form-select class="m-1" :options="roles" v-model="searchParam.role"></b-form-select>
                                    
                            <b-button class="m-1" type="submit" variant="primary">Pretra\u017Ei</b-button>
    						<b-button class="m-1" type="reset" variant="secondary">Resetuj pretragu</b-button>
                    	</b-form>
                    </b-col>
                </b-row>

                <b-row>
                    <b-col>
                        <dl>
                            <dd v-for="user in users">
                                <b-card style="margin-left:1%;margin-top:5%;margin-right:20%;">
                                    <h1 style="font-size:25px;">{{user.name}} {{user.surname}} <b-badge variant="primary">{{user.role}}</b-badge></h1>
                                    <label>Korisnicko ime </label><b> {{user.username}}</b>
                                    <br>
                                    <label>gender</label><b> {{user.gender}}</b>
    							</b-card>
                            </dd>
                        </dl>
                    </b-col>                
                </b-row>
            </b-container>
    `
});