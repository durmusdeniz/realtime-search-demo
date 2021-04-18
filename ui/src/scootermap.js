import * as React from 'react';
import { Row, Col, Button} from "react-bootstrap";
const axios = require("axios");


export class ScooterMap extends React.Component {
    mapRef = React.createRef();
    userLatLonRef = React.createRef();
    radiusRef = React.createRef();
    scooterCountRef = React.createRef();
    state = {
        map: null,
        H: null
    };

    componentDidMount() {
        const H = window.H;
        const platform = new H.service.Platform({
            apikey: process.env.REACT_APP_HERE_API_KEY
        });

        const defaultLayers = platform.createDefaultLayers();

        const map = new H.Map(
            this.mapRef.current,
            defaultLayers.vector.normal.map,
            {
                //Center of Singapore
                center: { lat: 1.3521, lng: 103.8198 },
                zoom: 12,
                pixelRatio: window.devicePixelRatio || 1
            }
        );


        // Behavior implements default interactions for pan/zoom (also on mobile touch environments)
        const behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));

        // Create the default UI components to allow the user to interact with them
        const ui = H.ui.UI.createDefault(map, defaultLayers);

        this.setState({ map:map, H:H });

    }

    componentWillUnmount() {
        this.state.map.dispose();
    }

    startSearch() {
        if(this.userLatLonRef.current.value === ""){
            alert("No value for user lat,lon!");
        }else{

            this.state.map.removeObjects(this.state.map.getObjects());
            let searchRadius = this.radiusRef.current.value;
            let scooterAmount = this.scooterCountRef.current.value;
            let lat = this.userLatLonRef.current.value.split(",")[0];
            let lon = this.userLatLonRef.current.value.split(",")[1];
            axios.get("http://localhost:8080/search/"+scooterAmount+"/"+searchRadius+"/"+lat+"/"+lon)
                .then((response) =>{

                   let bikes = response.data.split(";");

                    for (let bike in bikes) {
                         var icon = new this.state.H.map.Icon('kick-scooter.png');
                         var marker = new this.state.H.map.Marker({ lat: bikes[bike].split(",")[0], lng: bikes[bike].split(",")[1] }, { icon: icon });
                         this.state.map.addObject(marker);
                    }
                }).catch(function (error) {
                    console.log(error);
            }).then(function () {});

            //User's location on the map
            var pin = new this.state.H.map.Icon('pin.png');
            var pinMark = new this.state.H.map.Marker({ lat: lat, lng: lon }, { icon: pin });
            this.state.map.addObject(pinMark);
        }

    }

    render() {
        return (
            <>
            <Row className="justify-content-center">
                <div ref={this.mapRef} style={{ height: "500px", width:"700px" }} />
            </Row>
                <Row className="justify-content-center">
                    <Col>
                        <div className="input-group">
                            <input ref={this.scooterCountRef} type="number" className="form-control" placeholder="Amount of scooters to retrieve" />
                        </div>
                    </Col>
                </Row>
                <Row className="justify-content-center">
                    <Col>
                        <div className="input-group">
                            <input ref={this.userLatLonRef} type="text" className="form-control" placeholder="Enter lat,lon for the user" />
                        </div>
                    </Col>
                </Row>
                <Row className="justify-content-center">
                    <Col>
                        <div className="input-group">
                            <input ref={this.radiusRef} type="number" className="form-control" placeholder="Search radius in meters" />
                        </div>
                    </Col>
                </Row>
                <Row className="justify-content-center">
                    <Button onClick={() => this.startSearch()}  variant="primary">Search</Button>
                </Row>

            </>
            );
    }
}