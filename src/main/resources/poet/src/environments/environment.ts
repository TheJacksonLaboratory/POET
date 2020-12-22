// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
const POET_BASE_URL = 'http://localhost:8080/api/v1';
const MONARCH_BASE_URL = 'https://api.monarchinitiative.org/api';
const PUBMED_BASE_URL = 'https://eutils.ncbi.nlm.nih.gov/entrez/eutils/';
const HPO_BASE_URL = 'https://hpo.jax.org/api/';
import { domain, clientId, audience } from '../../auth.config.json';
export const environment = {
  auth: {
    domain,
    clientId,
    audience
  },
  httpInterceptor: {
    allowedList: [POET_BASE_URL + "/*"]
  },
  production: false,
  POET_BASE_URL: POET_BASE_URL,
  POET_API_CHECK_USER_URL: POET_BASE_URL + '/user/check',
  POET_API_SEARCH_URL: POET_BASE_URL + '/search',
  POET_API_MAXO_ANNOTATION: POET_BASE_URL + '/annotation/maxo/',
  POET_API_DISEASE_ENTITY_URL: POET_BASE_URL + '/entity/disease/',
  POET_API_PUBLICATION_ENTITY_URL: POET_BASE_URL + '/entity/publication/',
  POET_API_STATISTICS_USERACTIVITY_URL: POET_BASE_URL + "/statistics/user-activity/",
  POET_API_STATISTICS_CONTRIBUTION_URL: POET_BASE_URL + "/statistics/contributions/",
  HPO_API_HPO_SEARCH_URL: HPO_BASE_URL + 'hpo/search',
  HPO_API_MAXO_SEARCH_URL: HPO_BASE_URL + 'maxo/search/',
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
