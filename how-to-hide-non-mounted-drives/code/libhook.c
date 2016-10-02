#define _GNU_SOURCE
#include <stdio.h>
#include <stdint.h>
#include <dlfcn.h>

#include <gio/gio.h>
 
GList *
g_drive_get_volumes (GDrive *drive) {
    printf("> inside shared object...\n");
    char *identifier = g_drive_get_identifier (drive, G_VOLUME_IDENTIFIER_KIND_UNIX_DEVICE);
    printf("     drive: %s\n", identifier);
    g_free(identifier);
    
    static GList* (*original_g_drive_get_volumes)(GDrive*) = NULL;
    original_g_drive_get_volumes = dlsym(RTLD_NEXT, "g_drive_get_volumes");
    
    GList* original_volumes = original_g_drive_get_volumes(drive);
    GList* only_mounted_volumes = NULL;
    
    GList *l;
    
	if (original_volumes != NULL) {
		for (l = original_volumes; l != NULL; l = l->next) {
			GVolume *volume = l->data;
		        GMount *mount = g_volume_get_mount(volume);

			if (mount != NULL) {
				only_mounted_volumes = g_list_append (only_mounted_volumes, volume);
				g_object_unref(mount);
			} else {
				g_object_unref(volume);
			}
		}
	}
         
    printf("< returning from shared object...\n");
    return only_mounted_volumes;
} 
