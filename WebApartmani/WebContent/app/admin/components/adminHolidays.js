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
                    error => this.setAlert("Do\u0161lo je do gre\u0161ke pri u\u010Ditavanju praznika."));
        },
        save(){
            let datesForSending = [];
            for(date of this.dates){
                datesForSending.push(date.getTime() - date.getTimezoneOffset()*60*1000);
            }
            axios.put("/WebApartmani/rest/holidays", datesForSending).then(
                response => this.setAlert("Praznici su uspe\u0161no sa\u010Duvani.")).catch(
                    error => this.setAlert("Nije uspelo \u010Duvanje praznika: " + error.response.data));
        },
        fetched(newDates){
            this.dates = [];
            for(date of newDates){
            	let realDate = new Date(date);
                this.dates.push(new Date(realDate.getTime() + realDate.getTimezoneOffset()*60*1000));
            }
        },
        setAlert(text){
            this.alertText = text;
            this.alertShow = true;
        }
    },
    template:`
    <b-container class="w-50 p-5">
    <b-row>
    <b-alert
        v-model="alertShow"
        dismissible>
        {{ alertText }}
        </b-alert>
        <br>
        <b-button block v-on:click="save" variant="primary">Sa\u010Duvaj izmene</b-button>
        <br>
        <b-button block v-on:click="fetch" variant="secondary">Odustani od izmena</b-button>
        <br>
        <v-date-picker
        v-model="dates"
        mode="multiple"
        is-inline
        />
  </b-col> 
  </b-row>
</b-container>   
    `

});