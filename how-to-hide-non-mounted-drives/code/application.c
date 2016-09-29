#include <stdio.h>
#include <glib-object.h>
#include <gio/gio.h>


void
print_connected_drives (GList * connected_drives)
{
  GList *l, *ll;

  

  for (l = connected_drives; l != NULL; l = l->next)
    {
      GDrive *drive = l->data;
      GList *volumes = g_drive_get_volumes (drive);
      if (volumes != NULL)
	{
	  for (ll = volumes; ll != NULL; ll = ll->next)
	    {
	      GVolume *volume = ll->data;
	      char *identifier = g_volume_get_identifier (volume,
							  G_VOLUME_IDENTIFIER_KIND_CLASS);

	      // Skip network volumes
	      if (g_strcmp0 (identifier, "network") == 0)
		{
		  g_free (identifier);
		  continue;
		}
	      g_free (identifier);

	      GMount *mount = g_volume_get_mount (volume);
	      if (mount != NULL)
		{
		  GFile *root = g_mount_get_default_location (mount);
		  // TODO print root
		  g_object_unref (root);
		  char *mount_uri = g_file_get_uri (root);
		  // TODO print mount_uri
		  g_free (mount_uri);
		}

	      g_object_unref (mount);
	      g_object_unref (volume);
	    }
	  g_list_free (volumes);
	}
    }
  return;
}

int
main ()
{
  printf ("Hi! I'm sample application!\n");
  GVolumeMonitor *volume_monitor = g_volume_monitor_get ();

/////////////////////////////////////////////////////////////////////

  printf ("Call g_volume_monitor_get_connected_drives() ...\n");
  GList *connected_drives =
    g_volume_monitor_get_connected_drives (volume_monitor);
  print_connected_drives (connected_drives);

/////////////////////////////////////////////////////////////////////

  printf ("Bye!\n");
  g_list_free (connected_drives);
  g_object_unref (volume_monitor);


  return 0;
}
