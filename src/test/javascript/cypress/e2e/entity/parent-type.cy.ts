import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ParentType e2e test', () => {
  const parentTypePageUrl = '/tag/parent-type';
  const parentTypePageUrlPattern = new RegExp('/tag/parent-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const parentTypeSample = {
    topic: 'Refined Moldova',
    parentId: 35300,
    parentType: 'ADP deposit generate',
    server: 'Market Fantastic Coordinator',
    userManageable: false,
    isEncrypted: true,
  };

  let parentType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/tag/api/parent-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/tag/api/parent-types').as('postEntityRequest');
    cy.intercept('DELETE', '/services/tag/api/parent-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (parentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/tag/api/parent-types/${parentType.id}`,
      }).then(() => {
        parentType = undefined;
      });
    }
  });

  it('ParentTypes menu should load ParentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tag/parent-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ParentType').should('exist');
    cy.url().should('match', parentTypePageUrlPattern);
  });

  describe('ParentType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(parentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ParentType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tag/parent-type/new$'));
        cy.getEntityCreateUpdateHeading('ParentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', parentTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/tag/api/parent-types',
          body: parentTypeSample,
        }).then(({ body }) => {
          parentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/tag/api/parent-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/tag/api/parent-types?page=0&size=20>; rel="last",<http://localhost/services/tag/api/parent-types?page=0&size=20>; rel="first"',
              },
              body: [parentType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(parentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ParentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('parentType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', parentTypePageUrlPattern);
      });

      it('edit button click should load edit ParentType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ParentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', parentTypePageUrlPattern);
      });

      it('edit button click should load edit ParentType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ParentType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', parentTypePageUrlPattern);
      });

      it('last delete button click should delete instance of ParentType', () => {
        cy.intercept('GET', '/services/tag/api/parent-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('parentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', parentTypePageUrlPattern);

        parentType = undefined;
      });
    });
  });

  describe('new ParentType page', () => {
    beforeEach(() => {
      cy.visit(`${parentTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ParentType');
    });

    it('should create an instance of ParentType', () => {
      cy.get(`[data-cy="topic"]`).type('Metal Enhanced primary').should('have.value', 'Metal Enhanced primary');

      cy.get(`[data-cy="parentId"]`).type('11583').should('have.value', '11583');

      cy.get(`[data-cy="parentType"]`).type('COM expedite').should('have.value', 'COM expedite');

      cy.get(`[data-cy="server"]`).type('Leone Concrete Berkshire').should('have.value', 'Leone Concrete Berkshire');

      cy.get(`[data-cy="userManageable"]`).should('not.be.checked');
      cy.get(`[data-cy="userManageable"]`).click().should('be.checked');

      cy.get(`[data-cy="isEncrypted"]`).should('not.be.checked');
      cy.get(`[data-cy="isEncrypted"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        parentType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', parentTypePageUrlPattern);
    });
  });
});
