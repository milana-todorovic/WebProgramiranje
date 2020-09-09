const ApartmentComponent={template: '<host-apartments></host-apartments>'}
const ReservationComponent={template: '<host-reservations></host-reservations>'}
const ApartmentDetailsComponent={template: '<host-apartmentDetails></host-apartmentDetails>'}
const UsersComponent={template:'<host-users></host-users>'}
const AddApartmentComponent={template:'<host-addApartment></host-addApartment'}


const router=new VueRouter({
    mode: 'hash',
    routes:[
        {path: '/apartments', component:ApartmentComponent},
        {path: '/reservations', component:ReservationComponent},
        {path: '/apartmentDetails', component:ApartmentDetailsComponent},
        {path:'/users',component:UsersComponent},
        {path:'/addApartment',component:AddApartmentComponent}
        
    ]
});

var appHost = new Vue({
    router,
	el : "#hostApp",
	data : {
		title : "Host naslov"
    },
	
});