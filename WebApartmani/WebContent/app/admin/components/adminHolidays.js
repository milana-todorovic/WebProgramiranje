Vue.component("admin-holidays",{
    data: function(){
        return{
            dates: [],
            alertShow: false,
            alertText: null
        }
    },
    created() {
    	this.fetch();
    },
    watch: {
        '$route': 'fetch'
    },
    methods:{
        fetch(){
            axios.get("/WebApartmani/rest/holidays").then(
                response => this.fetched(response.data)).catch(
                    error => this.setAlert("Došlo je do greške pri učitavanju praznika."));
        },
        save(){
            let datesForSending = [];
            for(date of this.dates){
                datesForSending.push(date.getTime() - date.getTimezoneOffset()*60*1000);
            }
            axios.put("/WebApartmani/rest/holidays", datesForSending).then(
                response => this.setAlert("Praznici su uspešno sačuvani.")).catch(
                    error => this.setAlert("Nije uspelo čuvanje praznika: " + error.response.data));
        },
        fetched(newDates){
            this.dates = [];
            for(date of newDates){
            	let realDate = new Date(date);
            	console.log(new Date(realDate.getTime() + realDate.getTimezoneOffset()*60*1000));
                this.dates.push(new Date(realDate.getTime() + realDate.getTimezoneOffset()*60*1000));
            }
        },
        setAlert(text){
            this.alertText = text;
            this.alertShow = true;
        }
    },
    template:`
    <div>
        <b-alert
        v-model="alertShow"
        dismissible>
        {{ alertText }}
        </b-alert>
        <b-button block v-on:click="save" variant="primary">Sačuvaj izmene</b-button>
        <b-button block v-on:click="fetch" variant="secondary">Odustani od izmena</b-button>
        <v-date-picker
        v-model="dates"
        mode="multiple"
        is-inline
        />
       </div>    
    `

});