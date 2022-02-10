import http from 'k6/http';
import { sleep } from 'k6';

export default function() {
  http.get('http://localhost:8080/'); // get request to input URI
  sleep(1); // sleep while a second
}
