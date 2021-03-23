// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
import { HttpMethod } from "@auth0/auth0-angular";

const POET_BASE_URL = 'http://localhost:8080/api/v1';
const MONARCH_BASE_URL = 'https://api.monarchinitiative.org/api';
const PUBMED_BASE_URL = 'https://eutils.ncbi.nlm.nih.gov/entrez/eutils/';
const HPO_BASE_URL = 'https://hpo.jax.org/api/';
export const environment = {
  auth: {
    domain: "dev-poet.us.auth0.com",
    clientId: "zAquxh5T1sPsoqBmSd8R7UasuFCcl9LY",
    audience: "poet-api-audience"
  },
  httpInterceptor: {
    allowedList: [
      POET_BASE_URL + '/user/check',
      POET_BASE_URL + '/statistics/activity/',
      POET_BASE_URL + '/statistics/contributions/',
      {
        uri: POET_BASE_URL + '/annotation/*',
        httpMethod: HttpMethod.Post,
      },
      {
        uri: POET_BASE_URL + '/annotation/*',
        httpMethod: HttpMethod.Put,
      },
      {
        uri: POET_BASE_URL + '/annotation/*',
        httpMethod: HttpMethod.Delete,
      },
      {
        uri: POET_BASE_URL + '/entity/disease/',
        httpMethod: HttpMethod.Put,
      },
      {
        uri: POET_BASE_URL + '/entity/publication/',
        httpMethod: HttpMethod.Post,
      },
    ]
  },
  production: false,
  POET_BASE_URL: POET_BASE_URL,
  POET_API_CHECK_USER_URL: POET_BASE_URL + '/user/check',
  POET_API_SEARCH_URL: POET_BASE_URL + '/search',
  POET_API_TREATMENTS_ANNOTATION: POET_BASE_URL + '/annotation/treatments/',
  POET_API_PHENOTYPES_ANNOTATION: POET_BASE_URL + '/annotation/phenotypes/',
  POET_API_DISEASE_ENTITY_URL: POET_BASE_URL + '/entity/disease/',
  POET_API_PUBLICATION_ENTITY_URL: POET_BASE_URL + '/entity/publication/',
  POET_API_STATISTICS_ACTIVITY_URL: POET_BASE_URL + "/statistics/activity/",
  POET_API_STATISTICS_CONTRIBUTION_URL: POET_BASE_URL + "/statistics/contributions/",
  POET_API_STATISTICS_ANNOTATION_URL: POET_BASE_URL + "/statistics/annotation/",
  HPO_API_HPO_SEARCH_URL: HPO_BASE_URL + 'hpo/search',
  HPO_API_MAXO_SEARCH_URL: HPO_BASE_URL + 'maxo/search/',
  HPO_API_HPO_SEARCH_DESCENDANTS_URL: HPO_BASE_URL + 'hpo/search/descendants/',
  MONARCH_SEARCH_URL: MONARCH_BASE_URL + '/search/entity/autocomplete/',
  MONARCH_ENTITY_URL: MONARCH_BASE_URL + '/bioentity/',
  PUBMED_SUMMARY_URL: PUBMED_BASE_URL + 'esummary.fcgi',
  AUDIENCE_ROLE: "https://poet.jax.org/role"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
