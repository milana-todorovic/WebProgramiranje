const ApartmentComponent={template:'<admin-apartments></admin-apartments>'}
const ApartmentDetailsComponent={template:'<admin-apartmentDetails></admin-apartmentDetails>'}
const UsersComponent={template:'<admin-users></admin-users>'}
const ReservationComponent={template:'<admin-reservations></admin-reservations>'}
const HostComponent={template:'<create-host></create-host>'}


const router=new VueRouter({
    mode: 'hash',
    routes:[
        {path:'/apartments',component:ApartmentComponent},
        {path:'/apartmentDetails',component:ApartmentDetailsComponent},
        {path:'/users',component:UsersComponent},
        {path:'/reservations',component:ReservationComponent},
        {path:'/host',component:HostComponent}        
    ]
});

var appAdmin = new Vue({
    router,
	el : "#adminApp",
	data : {
		title : "Admin naslov"
    },
	components: {ApartmentComponent, ApartmentDetailsComponent, UsersComponent, ReservationComponent, HostComponent}
});