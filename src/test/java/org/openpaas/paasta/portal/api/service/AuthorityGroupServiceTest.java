//package org.openpaas.paasta.portal.api.service;
//
//import org.cloudfoundry.identity.uaa.scim.ScimGroup;
//import org.cloudfoundry.identity.uaa.scim.ScimGroupMember;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//
//import static org.mockito.Matchers.anyList;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.when;
//
//
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class AuthorityGroupServiceTest {
//
//    @Mock
//    AuthorityGroupService authorityGroupService;
//
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGetAuthorityGroups() throws Exception {
//
//        Collection<ScimGroup> collection = new ArrayList<>();
//        when(authorityGroupService.getAuthorityGroups()).thenReturn(collection);
//
//        Collection<ScimGroup> result = authorityGroupService.getAuthorityGroups();
//        Assert.assertEquals(collection, result);
//    }
//
//    @Test
//    public void testCreateAuthorityGroup() throws Exception {
//        ScimGroup scimGroup = new ScimGroup();
//        when(authorityGroupService.createAuthorityGroup(anyString(), anyList())).thenReturn(scimGroup);
//        ScimGroup result = authorityGroupService.createAuthorityGroup("displayName", Arrays.<ScimGroupMember>asList(new ScimGroupMember()));
//        Assert.assertEquals(scimGroup.getId(), result.getId());
//    }
//
//    @Test
//    public void testDeleteAuthorityGroup() throws Exception {
//        authorityGroupService.deleteAuthorityGroup("groupGuid");
//    }
//
//    @Test
//    public void testAddGroupMembers() throws Exception {
//        when(authorityGroupService.addGroupMembers(anyString(), anyList())).thenReturn(null);
//        ScimGroup result = authorityGroupService.addGroupMembers("groupGuid", Arrays.<String>asList("String"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testDeleteGroupMembers() throws Exception {
//        when(authorityGroupService.deleteGroupMembers(anyString(), anyList())).thenReturn(null);
//        ScimGroup result = authorityGroupService.deleteGroupMembers("groupGuid", Arrays.<String>asList("String"));
//        Assert.assertEquals(null, result);
//    }
//
//
//}
