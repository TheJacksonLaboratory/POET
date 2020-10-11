// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
const POET_BASE_URL = 'http://localhost:8080/api/v1';
const MONARCH_BASE_URL = 'https://api.monarchinitiative.org/api';
const PUBMED_BASE_URL = 'https://eutils.ncbi.nlm.nih.gov/entrez/eutils/';
import { domain, clientId, audience } from '../../auth.config.json';
export const environment = {
  auth: {
    domain,
    clientId,
    audience
  },
  httpInterceptor: {
    allowedList: [POET_BASE_URL]
  },
  production: false,
  POET_API_SEARCH_URL: POET_BASE_URL + '/search',
  POET_API_SEARCH_ANNOTATION_SOURCE_URL: POET_BASE_URL + '/search/annotation-source',
  MONARCH_SEARCH_URL: MONARCH_BASE_URL + '/search/entity/autocomplete/',
  PUBMED_SUMMARY_URL: PUBMED_BASE_URL + 'esummary.fcgi'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
