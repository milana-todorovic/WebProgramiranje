Vue.component("admin-users",{
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
        },
        block(user){
        	axios({
        		  method: 'put',
        		  url: '/WebApartmani/rest/users/' + user.id + "/blocked",
        		  data: true,
        		  headers: {
        		        'Content-Type': 'application/json'
        		    }
        		}).then(
        			data => user.blocked = true).catch(error => console.log(alert(error.response.data)));
        },
        unblock(user){
        	axios({
      		  method: 'put',
      		  url: '/WebApartmani/rest/users/' + user.id + "/blocked",
      		  data: false,
      		  headers: {
      		        'Content-Type': 'application/json'
      		    }
      		}).then(
      			data => user.blocked = false).catch(error => console.log(alert(error.response.data)));
        },
        isBlockable(user){
        	return !user.blocked && !(user.role == "Administrator");
        },
        isUnblockable(user){
        	return user.blocked;
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
    									<b-button v-if="isUnblockable(user)" variant="success" v-on:click="unblock(user)">Odblokiraj</b-button>
    								    <b-button v-if="isBlockable(user)" variant="danger" v-on:click="block(user)">Blokiraj</b-button>
    							</b-card>
                            </dd>
                        </dl>
                    </b-col>                
                </b-row>
            </b-container>
    `
});